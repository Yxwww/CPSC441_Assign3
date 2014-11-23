package cpsc441.doNOTmodify;

import java.io.*;

public class LogFactory implements ILogFactory {

    /**
     * Apparently some of you are having trouble understanding this code
     *   As you might imagine the first two lines create a file in the file system
     *   Then it returns a PrintStream, a thing that can be used to write data to that file.
     *
     *  Example usage (strait out of the assignment):
     *
     *  <code>
     *  PrintStream log = ...;
     *  String prettydv = Util.printdv(2, mincost, nexthop);
     *  log.println(prettydv);
     *  log.printf("[%s] send %s\n", router_ID, dvr);
     *  </code>
     *
     *  For more information on the java PrintStream go to http://bit.ly/1twjZBf
     *
     */
    public PrintStream newLogFile(File file) throws FileNotFoundException, IOException {
        file.getAbsoluteFile().getParentFile().mkdirs();
        file.createNewFile();
        return new PrintStream(
                new FileOutputStream(
                        file
                )
        );
    }
}
