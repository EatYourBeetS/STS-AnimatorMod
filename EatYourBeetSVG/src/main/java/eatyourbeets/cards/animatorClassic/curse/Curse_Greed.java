//package eatyourbeets.cards.animatorClassic.curse;
//
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
//import eatyourbeets.cards.base.AnimatorClassicCard_Curse;
//import eatyourbeets.cards.base.EYBCardData;
//import eatyourbeets.cards.base.EYBCardTarget;
//import eatyourbeets.utilities.GameActions;
//
//public class Curse_Greed extends AnimatorClassicCard_Curse
//{
//    public static final EYBCardData DATA = Register(Curse_Greed.class).SetCurse(-2, EYBCardTarget.None);
//
//    public Curse_Greed()
//    {
//        super(DATA, false);
//
//        Initialize(0, 0, 2);
//
//        SetSeries(CardSeries.Konosuba);
//    }
//
//    @Override
//    public void triggerWhenDrawn()
//    {
//        super.triggerWhenDrawn();
//
//        GameActions.Bottom.Add(new RandomCostIncrease(1, false));
//        GameActions.Bottom.Flash(this);
//    }
//
//    @Override
//    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
//    {
//
//    }
//}