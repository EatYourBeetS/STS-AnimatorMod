package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.cards.Synergies;

public class GoblinShaman extends AnimatorCard_Status
{
    public static final String ID = CreateFullID(GoblinShaman.class.getSimpleName());

    public GoblinShaman()
    {
        super(ID, 1, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0,0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActionsHelper.DrawCard(AbstractDungeon.player, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActionsHelper.ApplyPower(p, p, new FrailPower(p,1, false),1);
        }
    }
}