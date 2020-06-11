package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class YukariYakumo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YukariYakumo.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.Self);

    private boolean fromSynergy = false;

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        SetUpgrade(0, 0, 1, 1);
        SetScaling(0, 0, 0);

        SetExhaust(true);
        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void Refresh(AbstractMonster mo) {
        super.Refresh(mo);
        if (HasSynergy()) {
            setCostForTurn(0);
            fromSynergy = true;
        } else {
            if (fromSynergy) {
                setCostForTurn(this.cost);
                this.isCostModifiedForTurn = false;
                fromSynergy = false;
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new WeakPower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new FrailPower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new VulnerablePower(player, magicNumber, false));
        GameActions.Bottom.StackPower(new InvertPower(player, secondaryValue));
    }

    public static class InvertPower extends AnimatorPower
    {
        public static final String ID = "animator:YukariYakumoPower";

        public InvertPower(AbstractCreature owner, int amount)
        {
            super(owner, YukariYakumo.DATA);
            this.amount = amount;
            this.isTurnBased = true;
            updateDescription();
        }

        @Override
        public void atEndOfRound()
        {
            super.atEndOfRound();

            GameActions.Bottom.ReducePower(this, 1);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }


}

