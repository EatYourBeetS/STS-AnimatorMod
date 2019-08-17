package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class AbstractEnums
{
    public static class Characters
    {
        @SpireEnum
        public static PlayerClass THE_ANIMATOR;

        @SpireEnum
        public static PlayerClass THE_UNNAMED;
    }

    public static class Cards
    {
        @SpireEnum
        public static AbstractCard.CardColor THE_ANIMATOR;

        @SpireEnum
        public static AbstractCard.CardColor THE_UNNAMED;
    }

    public static class Library
    {
        @SpireEnum
        public static CardLibrary.LibraryType THE_ANIMATOR;

        @SpireEnum
        public static CardLibrary.LibraryType THE_UNNAMED;
    }

    public static class Screens
    {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen PURGING_STONE_SCREEN;
    }

    public static class Rewards
    {
        @SpireEnum
        public static RewardItem.RewardType SYNERGY_CARDS;

        @SpireEnum
        public static RewardItem.RewardType SPECIAL_GOLD;
    }

    public static class CardTags
    {
        @SpireEnum
        public static AbstractCard.CardTags TEMPORARY;

        //@SpireEnum
        //public static AbstractCard.CardTags SHAPESHIFTER;

        //@SpireEnum
        //public static AbstractCard.CardTags UNOBTAINABLE;

        @SpireEnum
        public static AbstractCard.CardTags UNIQUE;

        @SpireEnum
        public static AbstractCard.CardTags PURGE;

        @SpireEnum
        public static AbstractCard.CardTags LOYAL;

        @SpireEnum
        public static AbstractCard.CardTags IMPROVED_STRIKE;

        @SpireEnum
        public static AbstractCard.CardTags IMPROVED_DEFEND;
    }
}
