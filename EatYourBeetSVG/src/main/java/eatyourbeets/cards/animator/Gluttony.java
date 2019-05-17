package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.VariableExhaustAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Gluttony extends AnimatorCard
{
    public static final String ID = CreateFullID(Gluttony.class.getSimpleName());

    public Gluttony()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 3);

        AddExtendedDescription();

        this.exhaust = true;
        this.tags.add(CardTags.HEALING);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new VariableExhaustAction(p, this.magicNumber, this, this::OnExhaust));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void OnExhaust(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this || cards == null || cards.size() == 0)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard c : cards)
        {
            switch (c.type)
            {
                case ATTACK:
                    GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                    break;

                case SKILL:
                    GameActionsHelper.AddToBottom(new AddTemporaryHPAction(p, p, 6));
                    break;

                case POWER:
                    GameActionsHelper.AddToBottom(new HealAction(p, p, 7));
                    break;

                case CURSE:
                    GameActionsHelper.GainBlock(p, 6);
                    break;

                case STATUS:
                    GameActionsHelper.GainEnergy(1);
                    break;
            }
        }
    }
}