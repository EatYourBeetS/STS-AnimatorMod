package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.KrulTepesAction;
import eatyourbeets.actions.common.OnTargetDeadAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;

import java.util.ArrayList;

public class KrulTepes extends AnimatorCard
{
    public static final String ID = CreateFullID(KrulTepes.class.getSimpleName());

    private static final AbstractRelic relicReward = new BloodVial();

    public KrulTepes()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(14,0, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
            DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn));
            AbstractDungeon.actionManager.addToBottom(new OnTargetDeadAction(m, damageAction, new KrulTepesAction(m, this)));

            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, this.magicNumber, false), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(4);
        }
    }

    public boolean CanGetReward()
    {
        for (RewardItem r : AbstractDungeon.getCurrRoom().rewards)
        {
            if (r.relic != null && r.relic.relicId.equals(relicReward.relicId))
            {
                return false;
            }
        }

        return true;
    }

    public void ObtainReward()
    {
        int ownedRelics = 0;
        ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
        for (AbstractRelic relic : relics)
        {
            if (relic.relicId.equals(relicReward.relicId))
            {
                ownedRelics += 1;
            }
            else if (relic.relicId.equals(ExquisiteBloodVial.ID))
            {
                ownedRelics = -1;
                break;
            }
        }

        if (ownedRelics > 3 && (ownedRelics >= 5 || AbstractDungeon.miscRng.randomBoolean()))
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new ExquisiteBloodVial());
        }
        else if (UnnamedReignRelic.IsEquipped())
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new AncientMedallion());
        }
        else
        {
            AbstractDungeon.getCurrRoom().addRelicToRewards(relicReward.makeCopy());
        }
    }
}