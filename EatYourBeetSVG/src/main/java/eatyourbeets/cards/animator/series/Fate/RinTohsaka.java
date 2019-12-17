package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.Aether;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public class RinTohsaka extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(RinTohsaka.class.getSimpleName(), EYBCardBadge.Drawn);

    public RinTohsaka()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2, 1);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }

    @SpireOverride
    protected void applyPowersToBlock()
    {
        float tmp = (float) this.baseBlock;

        tmp += Spellcaster.GetScaling();

        for (AbstractPower p : AbstractDungeon.player.powers)
        {
            tmp = p.modifyBlock(tmp);
        }

        if (tmp < 0.0F)
        {
            tmp = 0.0F;
        }

        this.block = MathUtils.floor(tmp);
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (p.orbs.size() > 0)
        {
            AbstractOrb orb = p.orbs.get(0);
            AbstractOrb copy = orb.makeCopy();

            copy.evokeAmount = orb.evokeAmount;
            copy.passiveAmount = orb.passiveAmount;

            GameActions.Bottom.ChannelOrb(copy, true);
        }

        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }

    private void TryAddOrb(AbstractOrb orb, int weight, WeightedList<AbstractOrb> orbs, ArrayList<AbstractOrb> exclusion)
    {
        for (AbstractOrb exclude : exclusion)
        {
            if (exclude != null && orb.ID.equals(exclude.ID))
            {
                return;
            }
        }

        orbs.Add(orb, weight);
    }
}