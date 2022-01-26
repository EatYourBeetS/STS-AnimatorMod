package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Chaos;
import pinacolada.powers.special.DainsleifAbyssPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Dainsleif extends PCLCard_UltraRare {
    public static PCLCardTooltip Phase;
    public static PowerStrings PhaseStrings;
    private static final CardGroup cardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final CardGroup upgradedCardChoices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static final PCLCardData DATA = Register(Dainsleif.class).SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact)
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
                Phase = new PCLCardTooltip(PhaseStrings.NAME, PCLJUtils.Format(PhaseStrings.DESCRIPTIONS[0],DainsleifAbyssPower.COUNTDOWN_AMT));
            });

    public Dainsleif() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0);
        SetAffinity_Dark(1);
        SetAffinity_Blue(1);
        SetDelayed(true);

        SetAffinityRequirement(PCLAffinity.General, 13);
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
        PCLActions.Bottom.ChannelOrb(new Chaos());

        if (CheckAffinity(PCLAffinity.General)) {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> DoAction(2));
        }
        else {
            DoAction(1);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        PCLActions.Bottom.ApplyPower(player, player, new DainsleifAbyssPower(player));
    }

    private void DoAction(int choices) {
        PCLActions.Bottom.SelectFromPile(name, choices, upgraded ? upgradedCardChoices : cardChoices)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.MakeCardInDrawPile(c);
                    }
                });
    }
}