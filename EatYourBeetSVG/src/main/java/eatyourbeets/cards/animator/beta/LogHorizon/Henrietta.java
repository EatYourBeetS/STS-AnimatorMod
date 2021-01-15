package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.interfaces.subscribers.OnStanceChangedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Henrietta extends AnimatorCard implements OnCostRefreshSubscriber, OnStanceChangedSubscriber
{
    public static final EYBCardData DATA = Register(Henrietta.class).SetPower(3, CardRarity.RARE);

    private int costModifier = 0;

    public Henrietta() {
        super(DATA);

        Initialize(0, 2, 1, 1);
        SetEthereal(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.StackPower(new HenriettaPower(p, secondaryValue));
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        costModifier = 0;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Henrietta copy = (Henrietta) super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        OnCostRefresh(this);
    }

    @Override
    public void OnStanceChanged(AbstractStance oldStance, AbstractStance newStance)
    {
        OnCostRefresh(this);
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int currentCost = (costForTurn - costModifier);

            if (!player.stance.ID.equals(NeutralStance.STANCE_ID))
            {
                costModifier = -1;
            }
            else
            {
                costModifier = 0;
            }

            if (!this.freeToPlayOnce)
            {
                this.setCostForTurn(currentCost + costModifier);
            }
        }
    }


    public static class HenriettaPower extends AnimatorPower {
        public HenriettaPower(AbstractPlayer owner, int amount) {
            super(owner, Henrietta.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = FormatDescription(0);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            int energy = EnergyPanel.getCurrentEnergy();
            if (energy > 0)
            {
                EnergyPanel.useEnergy(energy);
                flash();
                ChooseStance();
            }
        }
    }

    private static void ChooseStance()
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(CreateChoice(text[1], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)));
        group.addToBottom(CreateChoice(text[2], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID)));
        group.addToBottom(CreateChoice(text[3], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID)));
        group.addToBottom(CreateChoice(text[4], (c1, p1, m1) -> GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)));

        GameActions.Top.SelectFromPile(Henrietta.DATA.Strings.NAME, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.use(player, null);
            }
        });
    }

    private static AnimatorCard_Dynamic CreateChoice(String text, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onSelect)
    {
        return new AnimatorCardBuilder(Henrietta.DATA.ID)
        .SetProperties(CardType.SKILL, Henrietta.DATA.CardRarity, CardTarget.NONE)
        .SetCost(-1, 0)
        .SetOnUse(onSelect)
        .SetText(Ainz.DATA.Strings.NAME, text, text).Build();
    }
}