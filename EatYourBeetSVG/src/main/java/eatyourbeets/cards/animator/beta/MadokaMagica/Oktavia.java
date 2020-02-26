package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Oktavia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Oktavia.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Oktavia()
    {
        super(DATA);

        Initialize(13, 0, 1);
        SetUpgrade(3, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = player.hand.getCardsOfType(CardType.CURSE).size();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
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

            if (magicNumber > 0)
            {
                GameActions.Bottom.Draw(magicNumber);
            }

            //Deal damage equal to number of curses times magic number
            GameActions.Bottom.Callback(___ ->
            {
                magicNumber = player.hand.getCardsOfType(CardType.CURSE).size();

                if (magicNumber > 0)
                {
                    int[] multiDamage = DamageInfo.createDamageMatrix(13, true);

                    for (int i = 0; i < magicNumber; i++) {
                        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
                                .SetOptions(true, false);
                    }

                    GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
                }
            });
        });
    }
}
