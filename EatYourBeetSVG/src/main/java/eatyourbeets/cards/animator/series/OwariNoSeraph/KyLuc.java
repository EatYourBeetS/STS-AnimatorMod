package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class KyLuc extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyLuc.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public KyLuc()
    {
        super(DATA);

        Initialize(4, 0, 3, 3);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Dark(2, 0, 2);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) * magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCreature c: GameUtilities.GetAllCharacters(true)) {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, c, secondaryValue, AttackEffects.SLASH_VERTICAL);
        }

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.RED, Color.LIGHT_GRAY, Color.RED, Color.RED).duration * 0.6f));
    }
}