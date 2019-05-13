package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.SmallLaser2Effect;
import eatyourbeets.powers.PlayerStatistics;

import java.util.List;

public class Aisha extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Aisha.class.getSimpleName());

    public Aisha()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(2, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + PlayerStatistics.GetFocus(player));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int max = p.filledOrbCount();
        for (int i = 0; i < max; i++)
        {
             GameActionsHelper.VFX(new SmallLaser2Effect(p.hb.cX, p.hb.cY,
                    m.hb.cX + MathUtils.random(-0.05F, 0.05F),
                    m.hb.cY + MathUtils.random(-0.05F, 0.05F),
                    Color.VIOLET), 0.1F);

             GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        }

        if (ProgressBoost())
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 2 : 1;
    }
}