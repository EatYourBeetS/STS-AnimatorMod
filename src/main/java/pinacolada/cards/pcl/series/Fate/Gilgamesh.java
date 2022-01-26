package pinacolada.cards.pcl.series.Fate;

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
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Crystallize;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Gilgamesh extends PCLCard implements OnRelicObtainedSubscriber
{
    private static final FieldInfo<ArrayList<StoreRelic>> _relics = PCLJUtils.GetField("relics", ShopScreen.class);
    private static AbstractRelic lastRelicObtained = null;

    public static final PCLCardData DATA = Register(Gilgamesh.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Ranged, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();

    public Gilgamesh()
    {
        super(DATA);

        Initialize(3, 0, 1, 25);
        SetUpgrade(1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetUnique(true, -1);

        SetHitCount(3);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 7 == 0)
        {
            upgradeMagicNumber(1);
        }
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

            if (PCLGameEffects.TopLevelQueue.Count() < 5)
            {
                PCLGameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                PCLGameEffects.TopLevelQueue.ShowCardBriefly(makeStatEquivalentCopy(), pos_x, pos_y);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX(SFX.ATTACK_WHIRLWIND);
        PCLActions.Bottom.VFX(new WhirlwindEffect(), 0f);

        if (timesUpgraded >= 6)
        {
            PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
            PCLActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);

            PCLActions.Bottom.SFX(SFX.ATTACK_HEAVY);
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SPEAR)
                    .forEach(d -> d.SetSoundPitch(1.3f, 1.4f).SetVFXColor(Color.YELLOW)
                            .SetDamageEffect((c, __) -> PCLGameEffects.Queue.Add(VFX.IronWave(player.hb, c.hb))));
            PCLActions.Bottom.VFX(new CleaveEffect());
        }
        else
        {
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SPEAR).forEach(d -> d
                    .SetSoundPitch(1.3f, 1.4f).SetVFXColor(Color.YELLOW));
        }

        for (int i = 0; i < magicNumber; i++) {
            PCLActions.Bottom.MakeCardInDrawPile(new Crystallize());
        }
    }
}