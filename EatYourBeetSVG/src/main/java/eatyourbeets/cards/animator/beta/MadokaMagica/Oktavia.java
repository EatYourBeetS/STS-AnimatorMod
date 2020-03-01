package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Oktavia extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Oktavia.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Oktavia()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(2, 0, 0, 0);
        SetScaling(1,0,0);

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
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInHand(GameUtilities.GetRandomCurse());
        }

        GameActions.Bottom.Callback(__ ->
        {
            GameActions.Bottom.Draw(player.hand.getCardsOfType(CardType.CURSE).size())
            .AddCallback(___ ->
            {
                magicNumber = player.hand.getCardsOfType(CardType.CURSE).size();

                for (int i = 0; i < magicNumber; i++)
                {
                    GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
                    .SetOptions(true, false);
                }

                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            });
        });
    }
}
