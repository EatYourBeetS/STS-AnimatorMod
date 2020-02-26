package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Oktavia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Oktavia.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Oktavia()
    {
        super(DATA);

        Initialize(13, 0, 0);
        SetUpgrade(3, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //Add curses to hand
        for (int i = 0; i < 2; i++)
        {
            GameActions.Bottom.MakeCardInHand(GameUtilities.GetRandomCurse());
        }

        // This needs to happen after the curses are added
        GameActions.Bottom.Callback(__ ->
        {
            //Draw cards equal to number of curses
            int numCurses = player.hand.getCardsOfType(CardType.CURSE).size();
            if (numCurses > 0)
            {
                GameActions.Bottom.Draw(numCurses);

                //Deal damage equal to number of curses times magic number
                int[] multiDamage = DamageInfo.createDamageMatrix(numCurses * damage, true);
                GameActions.Bottom.DealDamageToAll(multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            }
        });
    }
}
