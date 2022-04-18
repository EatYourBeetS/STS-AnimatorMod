package eatyourbeets.misc.sts_exporter;

import java.io.File;

class ExportPath
{
    public String absolute;
    public String relative; // relative to sts_exporter root
    public String relativeToMod; // relative to mod subdirectory
    public String file; // filename

    ExportPath(String base, String modDir, String dir, String file)
    {
        this.relativeToMod = dir == null ? file : dir + "/" + file;
        this.relative = modDir == null ? this.relativeToMod : modDir + "/" + this.relativeToMod;
        this.absolute = base + "/" + relative;
        this.file = file;
    }

    // make directory containing this file
    boolean mkdir()
    {
        return new File(absolute).getParentFile().mkdirs();
    }

    String relativeTo(String dir)
    {
        // find common prefix
        int prefix = commonPrefix(dir, this.absolute);
        int n = prefix < dir.length() ? countDir(dir.substring(prefix)) : 0;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; ++i)
        {
            out.append("../");
        }
        out.append(this.absolute.substring(prefix));
        return out.toString();
    }

    private static int commonPrefix(String a, String b)
    {
        int pos, last = 0;
        for (pos = 0; pos < a.length() && pos < b.length() && a.charAt(pos) == b.charAt(pos); ++pos)
        {
            if (a.charAt(pos) == '/')
            {
                last = pos + 1;
            }
        }
        if (pos == a.length() && pos < b.length() && b.charAt(pos) == '/')
        {
            last = pos + 1;
        }
        return last;
    }

    private static int countDir(String a)
    {
        if (a.length() == 0)
        {
            return 0;
        }
        int count = 1;
        for (int i = 0; i < a.length(); ++i)
        {
            if (a.charAt(i) == '/')
            {
                count++;
            }
        }
        return count;
    }
}