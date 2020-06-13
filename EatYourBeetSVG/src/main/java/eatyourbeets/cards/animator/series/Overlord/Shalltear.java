package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.List;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Shalltear()
    {
        super(DATA);

        Initialize(3, 0, 3, 1);
        SetUpgrade(0, 0, 0);
        SetScaling(1, 1, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    protected void OnBeingDragged()
    {
        List<ModifyIntent> modifyintentList = new ArrayList<>();
        modifyintentList.add(new ModifyIntent(ModifyIntent.ModifyIntentType.Weak, 1));

        if (HasSynergy())
        {
            modifyintentList.add(new ModifyIntent(ModifyIntent.ModifyIntentType.Shackles, secondaryValue));
        }

        GameUtilities.ModifyIntentsPreview(TargetHelper.All(), modifyintentList);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect((enemy, aBoolean) ->
        {
            GameEffects.List.Add(new HemokinesisEffect(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
            GameActions.Bottom.ApplyWeak(player, enemy, 1);

            if (HasSynergy())
            {
                GameActions.Bottom.ReduceStrength(enemy, secondaryValue, true).SetForceGain(true);
            }
        });
    }
}