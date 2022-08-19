//package eatyourbeets.cards.animatorClassic.curse;
//
//import com.megacrit.cardcrawl.actions.common.HealAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import eatyourbeets.cards.base.AnimatorClassicCard_Curse;
//import eatyourbeets.cards.base.EYBCardData;
//import eatyourbeets.cards.base.EYBCardTarget;
//import eatyourbeets.utilities.GameActions;
//import eatyourbeets.utilities.GameUtilities;
//
//public class Curse_Nutcracker extends AnimatorClassicCard_Curse
//{
//    public static final EYBCardData DATA = Register(Curse_Nutcracker.class).SetCurse(-2, EYBCardTarget.None);
//
//    public Curse_Nutcracker()
//    {
//        super(DATA, true);
//
//        Initialize(0, 0, 3);
//
//        SetSeries(CardSeries.YoujoSenki);
//    }
//
//    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
//    {
//        if (dontTriggerOnUseCard)
//        {
//            for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
//            {
//                GameActions.Bottom.Add(new HealAction(m1, null, magicNumber));
//            }
//        }
//    }
//}