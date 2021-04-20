package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SanaeKochiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SanaeKochiya.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Miracle(), false);
    }

    public SanaeKochiya()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.Scry(magicNumber)
        .AddCallback(cards ->
        GameActions.Bottom.StackPower(new NextTurnMiracle(player, cards.size())));
    }

    public static class NextTurnMiracle extends AnimatorPower
    {
        public NextTurnMiracle(AbstractCreature owner, int amount)
        {
            super(owner, SanaeKochiya.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            for (int i = 0; i < amount; i++) {
                GameActions.Bottom.MakeCardInHand(new Miracle());
            }
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

