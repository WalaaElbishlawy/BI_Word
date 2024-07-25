package invertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    public static void main(String args[]) throws IOException
    {
        Index5 index = new Index5();
        String files = "C:\\Users\\lenovo\\Downloads\\IR\\S1-2_20200425_20201043_20201217_20201120_20200442\\tmp11\\rl\\collection\\";

        File file = new File(files);
        String[] fileList = file.list();

        // Check if fileList is not null before sorting //assignment 2
        if (fileList != null)
        {
            fileList = index.sort(fileList);
            index.N = fileList.length;

            for (int i = 0; i < fileList.length; i++)
            {
                fileList[i] = files + fileList[i];
            }
            index.buildIndex(fileList);
            index.store("index");
            index.printDictionary();

            String test3 = "data  should plain greatest comif";
            System.out.println("Boo0lean Model result = \n" + index.find_24_01(test3));

            String phrase = "";

            do
            {
                System.out.println("Print search phrase: ");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                phrase = in.readLine();
                if (!phrase.isEmpty())
                {
                    System.out.println("Boolean Model result = \n" + index.find_24_01(phrase));
                }
            }
            while (!phrase.isEmpty());
        }
        else
        {
            System.out.println("No files found in the directory.");
        }
    }
}
