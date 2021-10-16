package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Aisha extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Aisha.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeries(CardSeries.Elsword);

    public Aisha()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(2, 0, 1);

        SetAffinity_Fire();
        SetAffinity_Mind();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(GameUtilities.GetCommonOrbCount());
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();

        GainFMForEachCommonOrb();
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GainFMForEachCommonOrb();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < GameUtilities.GetCommonOrbCount(); i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).SetVFX(true, false)
            .SetDamageEffect(enemy ->
            {
                GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
            });
        }
    }

    private void GainFMForEachCommonOrb()
    {
        GameActions.Bottom.RaiseFireLevel(magicNumber * GameUtilities.GetCommonOrbCount());
        GameActions.Bottom.RaiseMindLevel(magicNumber * GameUtilities.GetCommonOrbCount());
    }
}