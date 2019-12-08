package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

public class Defend_Kancolle extends Defend
{
    public static final String ID = Register(Defend_Kancolle.class.getSimpleName());

    public Defend_Kancolle()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 5);

        SetHealing(true);
        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.GainBlock(this.block);

        // TODO: Could be an action

        if (EffectHistory.TryActivateLimited(cardID))
        {
            for(int i = 0; i < this.magicNumber; ++i)
            {
                AbstractDungeon.effectList.add(new GainPennyEffect(p.hb.cX, p.hb.cY + (p.hb.height / 2)));
            }

            p.gainGold(this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}