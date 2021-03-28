package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Soujiro_Isami;
import eatyourbeets.cards.animator.beta.special.Soujiro_Kawara;
import eatyourbeets.cards.animator.beta.special.Soujiro_Kyouko;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Soujiro extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new Soujiro_Isami(), false);
        DATA.AddPreview(new Soujiro_Kawara(), false);
        DATA.AddPreview(new Soujiro_Kyouko(), false);
    }

    public Soujiro()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(2, 0, 2);
        SetScaling(0,1, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();

        int synergyCount = 0;

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            if (HasSynergy(c))
            {
                synergyCount++;
            }
        }

        damage += synergyCount * magicNumber;

        return damage;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && CombatStats.TryActivateLimited(cardID))
        {
            GameEffects.List.ShowCopy(this);

            final float speed = Settings.ACTION_DUR_XFAST;

            GameActions.Top.MakeCard(new Soujiro_Isami(), player.drawPile)
                    .SetDuration(speed, true);
            GameActions.Top.MakeCard(new Soujiro_Kawara(), player.drawPile)
                    .SetDuration(speed, true);
            GameActions.Top.MakeCard(new Soujiro_Kyouko(), player.drawPile)
                    .SetDuration(speed, true);
        }
    }
}