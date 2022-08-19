//package eatyourbeets.cards.animatorClassic.curse;
//
//import com.megacrit.cardcrawl.cards.status.Dazed;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import eatyourbeets.cards.base.CardSeries;
//import eatyourbeets.cards.base.EYBCardData;
//import eatyourbeets.cards.base.EYBCardTarget;
//import eatyourbeets.cards.base.FakeAbstractCard;
//import eatyourbeets.utilities.GameActions;
//
//public class Curse_Dizziness extends AnimatorClassicCard_Curse
//{
//    public static final Dazed DAZED = new Dazed();
//    public static final EYBCardData DATA = Register(Curse_Dizziness.class).SetCurse(-2, EYBCardTarget.None);
//    static
//    {
//        DATA.CardRarity = CardRarity.SPECIAL;
//        DATA.AddPreview(new FakeAbstractCard(DAZED), false);
//    }
//
//    public Curse_Dizziness()
//    {
//        super(DATA, false);
//
//        SetSeries(CardSeries.TouhouProject);
//    }
//
//    @Override
//    public void triggerWhenDrawn()
//    {
//        super.triggerWhenDrawn();
//
//        GameActions.Bottom.MakeCardInDrawPile(DAZED.makeCopy());
//        GameActions.Bottom.Flash(this);
//    }
//
//    @Override
//    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
//    {
//
//    }
//}