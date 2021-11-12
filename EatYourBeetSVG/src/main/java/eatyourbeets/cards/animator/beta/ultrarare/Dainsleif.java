package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.animator.DainsleifAbyssPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Dainsleif extends AnimatorCard_UltraRare {
    public static EYBCardTooltip Phase;
    public static PowerStrings PhaseStrings;
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static final EYBCardData DATA = Register(Dainsleif.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data ->
            {
                cardChoices.group.add(new Traveler_Aether());
                cardChoices.group.add(new Traveler_Lumine());
                for (AbstractCard c : cardChoices.group) {
                    data.AddPreview(c, true);
                    c = c.makeCopy();
                    c.upgrade();
                    upgradedCardChoices.group.add(c);

                }
                PhaseStrings = GR.GetPowerStrings(DainsleifAbyssPower.POWER_ID);
                Phase = new EYBCardTooltip(PhaseStrings.NAME, JUtils.Format(PhaseStrings.DESCRIPTIONS[0],DainsleifAbyssPower.COUNTDOWN_AMT));
            });

    public Dainsleif() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0);
        SetAffinity_Dark(2);
        SetAffinity_Blue(2);
        SetDelayed(true);

        SetAffinityRequirement(Affinity.General, 13);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (tooltips != null && Phase != null && !tooltips.contains(Phase))
        {
            tooltips.add(Phase);
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.ChannelOrb(new Chaos());

        if (CheckAffinity(Affinity.General)) {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> DoAction(2));
        }
        else {
            DoAction(1);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.ApplyPower(player, player, new DainsleifAbyssPower(player));
    }

    private void DoAction(int choices) {
        GameActions.Bottom.SelectFromPile(name, choices, upgraded ? upgradedCardChoices : cardChoices)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards) {
                        GameActions.Bottom.MakeCardInDrawPile(c);
                    }
                });
    }
}