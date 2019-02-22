package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.MedicalKit;
import eatyourbeets.actions.ShuffleRandomGoblinAction;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.cards.Synergies;

public class GoblinChampion extends AnimatorCard_Status
{
    public static final String ID = CreateFullID(GoblinChampion.class.getSimpleName());

    public GoblinChampion()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (!this.dontTriggerOnUseCard)
        {
            if (p.hasRelic(MedicalKit.ID))
            {
                this.useMedicalKit(p);
            }
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new ShuffleRandomGoblinAction(1));
        }
    }
}