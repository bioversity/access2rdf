import com.healthmarketscience.jackcess.*;
import org.apache.commons.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;


public class AccessExport {
    public static void main(String []args) throws IOException {
        
        System.out.println("@base <http://data.bioversityinternational.org/collectingmissions/> .");
        System.out.println("");
        Iterator<Table> iter = DatabaseBuilder.open(new File(args[0])).iterator();
        while(iter.hasNext()) {
            Table table = iter.next();
            // find primary key
            List<? extends Column> columns = table.getColumns();
            String firstCol = "";
            for(Column col : columns) {
                firstCol = col.getName();
                break;
            }

            Iterator<Row> rows = table.iterator();
            while(rows.hasNext()) {
                Row row = rows.next();
                /*
                if(!table.getName().equals("Cassava_Descriptors")) {
                    continue;
                }
                */
                
                // each row
                String instanceId = String.valueOf(row.get(firstCol));
                instanceId = URLEncoder.encode(instanceId, "UTF-8");

                System.out.println("<" + table.getName() + "/" + instanceId + "> a <" + table.getName() + ">;");

                // we only add non-null elements to this list
                List<String> list = new ArrayList<String>();
                for(Map.Entry<String, Object> entry : row.entrySet()) {
                    if(entry.getValue() != null) {
                        
                        String key = String.valueOf(entry.getKey());
                        key = URLEncoder.encode(key, "UTF-8");

                        String value = String.valueOf(entry.getValue());
                        value = StringEscapeUtils.escapeJava(value);

                        list.add("    <" + table.getName() + "/" + key + "> \"\"\"" + value + "\"\"\"");
                    }
                }

                // loop over non-empty list
                int c = 0;
                for(String elem : list) {
                    c++;
                    System.out.print(elem);
                    if(c != list.size()) {
                        System.out.println(";");
                    }
                }
                System.out.println("");
                System.out.println("    .");
            }

        }
    }
}
