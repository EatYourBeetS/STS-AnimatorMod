package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Vash extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vash.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Trigun);

    public Vash()
    {
        super(DATA);

        Initialize(4, 0, 2, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        return super.ModifyDamage(enemy, damage + GameActionManager.totalDiscardedThisTurn * secondaryValue);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.GUNSHOT).SetSoundPitch(0.5f, 0.7f);
        }

        GameActions.Bottom.Reload(name, cards -> GameActions.Bottom.CreateThrowingKnives(cards.size(), player.discardPile).SetUpgrade(upgraded));
    }
}