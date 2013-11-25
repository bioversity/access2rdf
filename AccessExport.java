import com.healthmarketscience.jackcess.*;
import java.io.*;
import java.util.*;

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
                
                // each row
                System.out.println("<" + table.getName() + "/" + row.get(firstCol) + "> a <" + table.getName() + ">;");
                int c = 0;
                for(Map.Entry<String, Object> entry : row.entrySet()) {
                    c++;
                    if(entry.getValue() != null) {

                        System.out.print("    <" + table.getName() + "/" + entry.getKey() + "> \"\"\"" + entry.getValue() + "\"\"\"");
                        if(c != row.size()) {
                            System.out.println(";");
                        }
                    }
                }
                System.out.println("");
                System.out.println("    .");
            }

            // XXX stop first table
            break;
        }
    }
}
