package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Gilgamesh extends AnimatorCard implements OnRelicObtainedSubscriber
{
    private static final FieldInfo<ArrayList<StoreRelic>> _relics = JUtils.GetField("relics", ShopScreen.class);
    private static AbstractRelic lastRelicObtained = null;

    public static final EYBCardData DATA = Register(Gilgamesh.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Gilgamesh()
    {
        super(DATA);

        Initialize(2, 0, 3, 25);
        SetUpgrade(1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(2, 0, 1);
        SetAffinity_Green(1);

        SetUnique(true, true);
        SetDelayed(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (lastRelicObtained == relic)
        {
            return;
        }

        lastRelicObtained = relic;

        if (AbstractDungeon.shopScreen != null) for (StoreRelic sr : _relics.Get(AbstractDungeon.shopScreen))
        {
            if (sr.relic == relic)
            {
                return;
            }
        }

        if (!(relic instanceof UnnamedReignRelic))
        {
            final float pos_x = (float) Settings.WIDTH / 4f;
            final float pos_y = (float) Settings.HEIGHT / 2f;

            upgrade();

            player.bottledCardUpgradeCheck(this);
            player.gainGold(secondaryValue);

            if (GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                GameEffects.TopLevelQueue.ShowCardBriefly(makeStatEquivalentCopy(), pos_x, pos_y);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ATTACK_WHIRLWIND);
        GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

        if (timesUpgraded >= 6)
        {
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
            GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);

            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.SFX(SFX.ATTACK_HEAVY);
                GameActions.Bottom.DealDamageToAll(this, AttackEffects.SPEAR)
                .SetSoundPitch(1.3f, 1.4f).SetVFXColor(Color.YELLOW)
                .SetDamageEffect((c, __) -> GameEffects.Queue.Add(VFX.IronWave(player.hb, c.hb)));
                GameActions.Bottom.VFX(new CleaveEffect());
            }
        }
        else
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.DealDamageToAll(this, AttackEffects.SPEAR)
                .SetSoundPitch(1.3f, 1.4f).SetVFXColor(Color.YELLOW);
            }
        }
    }
}