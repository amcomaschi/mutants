package ar.com.mercadolibre.mutants.detector.gst;

import ar.com.mercadolibre.mutants.detector.Detector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class GeneralizedSuffixTree implements Detector{

    private int last = 0;

    private static final String[] MUTANT_DNA_SEQUENCES = {"AAAA", "CCCC", "GGGG", "TTTT"};

    private final Node root = new Node();

    private Node activeLeaf = root;

    public Collection<Integer> search(String word) {
        return search(word, -1);
    }

    @Override
    public boolean analyzeDNA(String[] dna) {

        boolean isMutant = false;
        int count = 0;
        ArrayList<String> words = getWords(dna);

        for (int i = 0; i < words.size(); i++) {
            this.put(words.get(i), i);
        }

        for (String mutantDnaItem : MUTANT_DNA_SEQUENCES) {
            count += this.getCount(mutantDnaItem);

            if(count >= 2) {
                return Boolean.TRUE;
            }
        }
        return isMutant;
    }

    private static ArrayList getWords(String[] dna) {

        long start = System.nanoTime();

        ArrayList<String> words = new ArrayList();

        // Agrego filas
        for(String s : dna) {
            words.add(s);
        }

        // Agrego columnas
        for(int row = 0; row < dna.length; row++) {

            StringBuffer strColumn = new StringBuffer(dna.length);

            for (int column = 0; column < dna.length; column++) {
                strColumn.append(dna[column].charAt(row));
            }

            words.add(strColumn.toString());
        }

        // Armar secuencias dna oblicuas
        for(int i = 0; i < dna.length / 2; i++) {
            StringBuffer obliqueDna1 = new StringBuffer(dna.length);
            StringBuffer obliqueDna2 = new StringBuffer(dna.length);

            for (int j = 0; j < dna.length -i; j++) {
                obliqueDna1.append(dna[j].charAt(j+i));

                if(i != 0) {
                    obliqueDna2.append(dna[i + j].charAt(j));
                }
            }

            if(obliqueDna1.length() > 0) {
                words.add(obliqueDna1.toString());
            }

            if(obliqueDna2.length() > 0) {
                words.add(obliqueDna2.toString());
            }
        }

        // for (String word : words) {
        //    System.out.println(word);
        //}

        //System.out.println("Finished words at " + (System.nanoTime() - start) / 1000000000f);
        return words;
    }

    public Collection<Integer> search(String word, int results) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return Collections.EMPTY_LIST;
        }
        return tmpNode.getData(results);
    }

    public int getCount(String word) {
        Node tmpNode = searchNode(word);
        int count = 0;
        if (tmpNode == null) {
            return 0;
        }

        count += countChildNodes(tmpNode);
        return count;

    }

    public int countChildNodes(Node n) {
        int count = 0;

        Iterator iteraEdges = n.getEdges().values().iterator();

        // It is a leaf edge
        if(n.getEdges().size() ==0) {
            return n.getData().size() * 1;
        }
        while (iteraEdges.hasNext()) {
            Edge e = (Edge) iteraEdges.next();

            if (e.getDest() != null) {
                count += countChildNodes(e.getDest());
            }
        }

        return count;
    }

    public ResultInfo searchWithCount(String word, int to) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return new ResultInfo(Collections.EMPTY_LIST, 0);
        }

        return new ResultInfo(tmpNode.getData(to), tmpNode.getSuffix().getResultCount());
    }

    private Node searchNode(String word) {

        Node currentNode = root;
        Edge currentEdge;

        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);

            currentEdge = currentNode.getEdge(ch);
            if (null == currentEdge) {
                return null;
            } else {
                String label = currentEdge.getLabel();
                int lenToMatch = Math.min(word.length() - i, label.length());
                if (!word.regionMatches(i, label, 0, lenToMatch)) {
                    return null;
                }

                if (label.length() >= word.length() - i) {
                    return currentEdge.getDest();
                } else {
                    currentNode = currentEdge.getDest();
                    i += lenToMatch - 1;
                }
            }
        }

        return null;
    }

    public void put(String key, int index) throws IllegalStateException {
        if (index < last) {
            throw new IllegalStateException("The input index must not be less than any of the previously inserted ones. Got " + index + ", expected at least " + last);
        } else {
            last = index;
        }

        activeLeaf = root;

        String remainder = key + "$";
        Node s = root;

        String text = "";
        for (int i = 0; i < remainder.length(); i++) {
            text += remainder.charAt(i);
            text = text.intern();

            Pair<Node, String> active = update(s, text, remainder.substring(i), index);
            active = canonize(active.getFirst(), active.getSecond());

            s = active.getFirst();
            text = active.getSecond();
        }

        if (null == activeLeaf.getSuffix() && activeLeaf != root && activeLeaf != s) {
            activeLeaf.setSuffix(s);
        }

    }

    private Pair<Boolean, Node> testAndSplit(final Node inputs, final String stringPart, final char t, final String remainder, final int value) {
        Pair<Node, String> ret = canonize(inputs, stringPart);
        Node s = ret.getFirst();
        String str = ret.getSecond();

        if (!"".equals(str)) {
            Edge g = s.getEdge(str.charAt(0));

            String label = g.getLabel();
            if (label.length() > str.length() && label.charAt(str.length()) == t) {
                return new Pair<Boolean, Node>(true, s);
            } else {
                String newlabel = label.substring(str.length());
                assert (label.startsWith(str));

                Node r = new Node();
                Edge newedge = new Edge(str, r);

                g.setLabel(newlabel);

                r.addEdge(newlabel.charAt(0), g);
                s.addEdge(str.charAt(0), newedge);

                return new Pair<Boolean, Node>(false, r);
            }

        } else {
            Edge e = s.getEdge(t);
            if (null == e) {
                return new Pair<Boolean, Node>(false, s);
            } else {
                if (remainder.equals(e.getLabel())) {
                    e.getDest().addRef(value);
                    return new Pair<Boolean, Node>(true, s);
                } else if (remainder.startsWith(e.getLabel())) {
                    return new Pair<Boolean, Node>(true, s);
                } else if (e.getLabel().startsWith(remainder)) {
                    Node newNode = new Node();
                    newNode.addRef(value);

                    Edge newEdge = new Edge(remainder, newNode);

                    e.setLabel(e.getLabel().substring(remainder.length()));

                    newNode.addEdge(e.getLabel().charAt(0), e);

                    s.addEdge(t, newEdge);

                    return new Pair<Boolean, Node>(false, s);
                } else {
                    return new Pair<Boolean, Node>(true, s);
                }
            }
        }

    }

    private Pair<Node, String> canonize(final Node s, final String inputstr) {

        if ("".equals(inputstr)) {
            return new Pair<Node, String>(s, inputstr);
        } else {
            Node currentNode = s;
            String str = inputstr;
            Edge g = s.getEdge(str.charAt(0));
            while (g != null && str.startsWith(g.getLabel())) {
                str = str.substring(g.getLabel().length());
                currentNode = g.getDest();
                if (str.length() > 0) {
                    g = currentNode.getEdge(str.charAt(0));
                }
            }

            return new Pair<Node, String>(currentNode, str);
        }
    }

    private Pair<Node, String> update(final Node inputNode, final String stringPart, final String rest, final int value) {
        Node s = inputNode;
        String tempstr = stringPart;
        char newChar = stringPart.charAt(stringPart.length() - 1);

        Node oldroot = root;

        Pair<Boolean, Node> ret = testAndSplit(s, tempstr.substring(0, tempstr.length() - 1), newChar, rest, value);

        Node r = ret.getSecond();
        boolean endpoint = ret.getFirst();

        Node leaf;
        while (!endpoint) {
            Edge tempEdge = r.getEdge(newChar);
            if (null != tempEdge) {
                leaf = tempEdge.getDest();
            } else {
                leaf = new Node();
                leaf.addRef(value);
                Edge newedge = new Edge(rest, leaf);
                r.addEdge(newChar, newedge);
            }

            if (activeLeaf != root) {
                activeLeaf.setSuffix(leaf);
            }
            activeLeaf = leaf;

            if (oldroot != root) {
                oldroot.setSuffix(r);
            }

            oldroot = r;

            if (null == s.getSuffix()) { // root node
                assert (root == s);
                tempstr = tempstr.substring(1);
            } else {
                Pair<Node, String> canret = canonize(s.getSuffix(), safeCutLastChar(tempstr));
                s = canret.getFirst();
                tempstr = (canret.getSecond() + tempstr.charAt(tempstr.length() - 1)).intern();
            }

            ret = testAndSplit(s, safeCutLastChar(tempstr), newChar, rest, value);
            r = ret.getSecond();
            endpoint = ret.getFirst();

        }

        if (oldroot != root) {
            oldroot.setSuffix(r);
        }
        oldroot = root;

        return new Pair<Node, String>(s, tempstr);
    }

    Node getRoot() {
        return root;
    }

    private String safeCutLastChar(String seq) {
        if (seq.length() == 0) {
            return "";
        }
        return seq.substring(0, seq.length() - 1);
    }

    public int computeCount() {
        return root.computeAndCacheCount();
    }

    public static class ResultInfo {

        public int totalResults;

        public Collection<Integer> results;

        public ResultInfo(Collection<Integer> results, int totalResults) {
            this.totalResults = totalResults;
            this.results = results;
        }
    }

    private class Pair<A, B> {

        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }
}
