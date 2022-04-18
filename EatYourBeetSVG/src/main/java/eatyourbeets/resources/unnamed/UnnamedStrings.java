package eatyourbeets.resources.unnamed;

import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;

public class UnnamedStrings
{
    public Misc Misc;
    public Chat Chat;

    public void Initialize()
    {
        Misc = new Misc();
        Chat = new Chat();
    }

    public class Misc
    {
        //private final UIStrings Strings = GetUIStrings("Misc");
    }

    public class Chat
    {
        private final UIStrings Strings = GetUIStrings("Chat");

        public final String Defeated = Strings.TEXT[0];
        public final String UltimateShape = Strings.TEXT[1];
        public final String UnnamedReign = Strings.TEXT[2];
        public final String ToOtherMe1 = Strings.TEXT[3];
        public final String OtherMeReply = Strings.TEXT[4];
        public final String ToOtherMe2 = Strings.TEXT[5];
    }

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(UnnamedResources.ID, id));
    }
}