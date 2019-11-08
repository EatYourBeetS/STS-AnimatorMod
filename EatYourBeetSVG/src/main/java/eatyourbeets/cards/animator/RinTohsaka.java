package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.orbs.Air;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.common.TemporaryArtifactPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public class RinTohsaka extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(RinTohsaka.class.getSimpleName(), EYBCardBadge.Synergy);

    public RinTohsaka()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 6);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        WeightedList<AbstractOrb> orbs = new WeightedList<>();
        TryAddOrb(new Lightning (), 7, orbs, p.orbs);
        TryAddOrb(new Frost(), 7, orbs, p.orbs);
        TryAddOrb(new Earth(), 6, orbs, p.orbs);
        TryAddOrb(new Fire(), 6, orbs, p.orbs);
        TryAddOrb(new Dark(), 4, orbs, p.orbs);
        TryAddOrb(new Air(), 4, orbs, p.orbs);
        TryAddOrb(new Plasma(), 2, orbs, p.orbs);

        for (int i = 0; i < magicNumber; i++)
        {
            AbstractOrb orb = orbs.Retrieve(AbstractDungeon.cardRandomRng);
            if (orb != null)
            {
                GameActionsHelper.ChannelOrb(orb, true);
            }
        }

        if (HasActiveSynergy())
        {
            TemporaryArtifactPower.Apply(p, p, 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
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