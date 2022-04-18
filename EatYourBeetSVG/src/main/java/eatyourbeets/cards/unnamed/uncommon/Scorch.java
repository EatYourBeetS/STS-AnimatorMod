package eatyourbeets.cards.unnamed.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.actions.basic.RemoveBlock;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Scorch extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Scorch.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .ObtainableAsReward((data, deck) -> deck.size() >= (12 + (12 * data.GetTotalCopies(deck))));

    public Scorch()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && GameUtilities.GetPowerAmount(m, StrengthPower.POWER_ID) > 0)
        {
            GameUtilities.GetIntent(m).AddStrength(-1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(VFX.Fireball(player.hb, m.hb).SetColor(Color.RED, Color.ORANGE).SetScale(2).SetRealtime(true));
        GameActions.Bottom.Add(new RemoveBlock(m, p)).SetVFX(true, true);
        GameActions.Bottom.VFX(new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.3f, true);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.Callback(m, (c, __) ->
        {
            if (GameUtilities.GetPowerAmount(c, StrengthPower.POWER_ID) > 0)
            {
                GameActions.Bottom.ReduceStrength(player, c, magicNumber, false);
            }
        });
    }
}