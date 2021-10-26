package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class KyLuc extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyLuc.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public KyLuc()
    {
        super(DATA);

        Initialize(8, 0, 4);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Fire();
        SetAffinity_Poison();
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCreature c: GameUtilities.GetAllCharacters(true)) {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, c, magicNumber, AttackEffects.SLASH_VERTICAL);
        }

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.RED, Color.LIGHT_GRAY, Color.RED, Color.RED).duration * 0.6f);
    }
}