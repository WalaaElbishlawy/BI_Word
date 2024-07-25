package invertedIndex;
import java.util.*;
import java.io.*;

public class Index5 {

    int N = 0;
    public Map<Integer, SourceRecord> sources;
    public HashMap<String, DictEntry> index;

    public Index5() {
        sources = new HashMap<Integer, SourceRecord>();
        index = new HashMap<String, DictEntry>();
    }

    // Other methods...

    // Method to store the index in a text file
    public String[] sort(String[] fileList) {
        Arrays.sort(fileList);
        return fileList;
    }
    public void store(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Iterator<Map.Entry<String, DictEntry>> iterator = index.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, DictEntry> entry = iterator.next();
                writer.write(entry.getKey() + ":" + entry.getValue().doc_freq + ":");
                Posting posting = entry.getValue().pList;
                while (posting != null) {
                    writer.write("(" + posting.docId + "," + posting.dtf + ")");
                    posting = posting.next;
                    if (posting != null) {
                        writer.write(",");
                    }
                }
                writer.write("\n"); // Use "\n" instead of newLine()
            }
            System.out.println("Index stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing index: " + e.getMessage());
        }
    }


    public void setN(int n) {
        N = n;
    }

    public void printPostingList(Posting p) {
        System.out.print("[");
        boolean first = true;
        while (p != null) {
            if (!first) {
                System.out.print(",");
            } else {
                first = false;
            }
            System.out.print("" + p.docId);
            p = p.next;
        }
        System.out.println("]");
    }

    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry dd = (DictEntry) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "]       =--> ");
            printPostingList(dd.pList);
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());
    }

    public void buildIndex(String[] files) {
        int fid = 0;
        for (String fileName : files) {
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                if (!sources.containsKey(fileName)) {
                    sources.put(fid, new SourceRecord(fid, fileName, fileName, "notext"));
                }
                String ln;
                int flen = 0;
                int position = 0; // Track position within document
                while ((ln = file.readLine()) != null) {
                    flen += indexOneLine(ln, fid, position); // Update document length
                    position++; // Increment position
                }
                sources.get(fid).length = flen;

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            fid++;
        }
        printDictionary();
    }

    public int indexOneLine(String ln, int fid, int position) {
        int flen = 0;

        if (ln != null && !ln.isEmpty()) {
            String[] words = ln.split("\\W+");
            flen += words.length;
            for (int i = 0; i < words.length; i++) {
                String word = words[i].toLowerCase();
                if (!stopWord(word)) {
                    word = stemWord(word);

                    if (!index.containsKey(word)) {
                        index.put(word, new DictEntry());
                    }
                    if (!index.get(word).postingListContains(fid)) {
                        index.get(word).doc_freq += 1;
                        if (index.get(word).pList == null) {
                            index.get(word).pList = new Posting(fid, position); // Store position
                            index.get(word).last = index.get(word).pList;
                        } else {
                            index.get(word).last.next = new Posting(fid, position); // Store position
                            index.get(word).last = index.get(word).last.next;
                        }
                    } else {
                        index.get(word).last.dtf += 1;
                    }
                    index.get(word).term_freq += 1;

                    if (i < words.length - 1) {
                        String nextWord = words[i + 1].toLowerCase();
                        String biWord = word + "_" + nextWord;

                        if (!index.containsKey(biWord)) {
                            index.put(biWord, new DictEntry());
                        }
                        if (!index.get(biWord).postingListContains(fid)) {
                            index.get(biWord).doc_freq += 1;
                            if (index.get(biWord).pList == null) {
                                index.get(biWord).pList = new Posting(fid, position); // Store position
                                index.get(biWord).last = index.get(biWord).pList;
                            } else {
                                index.get(biWord).last.next = new Posting(fid, position); // Store position
                                index.get(biWord).last = index.get(biWord).last.next;
                            }
                        } else {
                            index.get(biWord).last.dtf += 1;
                        }
                        index.get(biWord).term_freq += 1;
                    }
                }
                position++; // Increment position for each word
            }
        }
        return flen;
    }

    boolean stopWord(String word) {
        if (word.equals("the") || word.equals("to") || word.equals("be") || word.equals("for") || word.equals("from")
                || word.equals("in") || word.equals("a") || word.equals("into") || word.equals("by") || word.equals("or")
                || word.equals("and") || word.equals("that")) {
            return true;
        }
        if (word.length() < 2) {
            return true;
        }
        return false;
    }

    //  stem a word (currently not implemented)
    String stemWord(String word) { //skip for now
        return word;
//        Stemmer s = new Stemmer();
//        s.addString(word);
//        s.stem();
//        return s.toString();
    }

    Posting intersect(Posting pL1, Posting pL2) {
        Posting answer = null;
        Posting last = null;

        while (pL1 != null && pL2 != null) {
            if (pL1.docId == pL2.docId) {
                if (answer == null) {
                    answer = new Posting(pL1.docId, pL1.position);
                    last = answer;
                } else {
                    last.next = new Posting(pL1.docId, pL1.position);
                    last = last.next;
                }
                pL1 = pL1.next;
                pL2 = pL2.next;
            } else if (pL1.docId < pL2.docId) {
                pL1 = pL1.next;
            } else {
                pL2 = pL2.next;
            }
        }
        return answer;
    }

    //  find documents containing a given phrase
    // we change in this function
    public String find_24_01(String phrase) {
        String result = "";
        String[] words = phrase.split("\\W+");

        // Create a HashSet to store unique document IDs
        HashSet<Integer> uniqueDocIds = new HashSet<>();

        // Intersect postings for each word in the phrase
        Posting posting = null;
        for (String word : words) {
            DictEntry entry = index.get(word.toLowerCase());
            if (entry != null && entry.pList != null) {
                Posting wordPosting = entry.pList;
                if (posting == null) {
                    posting = wordPosting;
                } else {
                    posting = intersect(posting, wordPosting);
                }
            }
        }

        // Add unique document IDs to the HashSet
        while (posting != null) {
            uniqueDocIds.add(posting.docId);
            posting = posting.next;
        }

        // Print the documents corresponding to unique document IDs
        for (Integer docId : uniqueDocIds) {
            result += "\t" + docId + " - " + sources.get(docId).title + " - " + sources.get(docId).length + "\n";
        }

        return result;
    }

    // Your sort, store, storageFileExists, createStore, and load methods go here

}
