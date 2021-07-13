package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
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
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Gilgamesh()
    {
        super(DATA);

        Initialize(3, 0, 3, 25);
        SetUpgrade(1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Dark(1);

        SetUnique(true, true);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (timesUpgraded >= 8)
        {
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
            GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.1f);
            GameActions.Bottom.SFX("ATTACK_WHIRLWIND");
            GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.SFX("ATTACK_HEAVY");
                GameActions.Bottom.VFX(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX), 0.1f);
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.VFX(new CleaveEffect());
            }
        }
        else
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            }
        }
    }
}