package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Henrietta extends AnimatorCard {
    public static final EYBCardData DATA = Register(Henrietta.class).SetPower(2, CardRarity.RARE);

    public Henrietta() {
        super(DATA);

        Initialize(0, 2, 1, 2);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        EnterRandomStanceNotCurrent();
        GameActions.Bottom.Motivate(magicNumber);

        GameActions.Bottom.StackPower(new HenriettaPower(p, secondaryValue));
    }

    private void EnterRandomStanceNotCurrent()
    {
        RandomizedList<String> stances = new RandomizedList<>();

        if (!player.stance.ID.equals(ForceStance.STANCE_ID))
        {
            stances.Add(ForceStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            stances.Add(AgilityStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(IntellectStance.STANCE_ID))
        {
            stances.Add(IntellectStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID))
        {
            stances.Add(NeutralStance.STANCE_ID);
        }

        GameActions.Bottom.ChangeStance(stances.Retrieve(rng));
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
            if (energy > 1)
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

        GameActions.Top.SelectFromPile(Ainz.DATA.Strings.NAME, 1, group)
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
        .SetCost(-2, 0)
        .SetOnUse(onSelect)
        .SetText(Ainz.DATA.Strings.NAME, text, text).Build();
    }
}