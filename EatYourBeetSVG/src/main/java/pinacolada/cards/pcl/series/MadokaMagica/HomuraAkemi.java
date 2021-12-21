package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.HomuraAkemi_Homulily;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HomuraAkemi extends PCLCard
{
    public static final PCLCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new HomuraAkemi_Homulily(), true);
            });

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 2, 2, 3);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Light(0,0,1);
        SetAffinity_Silver(1, 0, 0);

        SetDelayed(true);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 8);
        SetSoul(2, 0, HomuraAkemi_Homulily::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup[] choices = upgraded ? new CardGroup[] {player.hand,player.drawPile,player.discardPile,player.exhaustPile} : new CardGroup[] {player.hand,player.drawPile,player.discardPile};
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.PurgeFromPile(name,1,choices).SetFilter(c -> CardType.CURSE.equals(c.type)).AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        PCLActions.Bottom.ApplyPower(new HomuraAkemiPower(player, this, magicNumber));
                    }
                });

        if (TrySpendAffinity(PCLAffinity.Light) && info.TryActivateLimited()) {
            PCLActions.Bottom.GainArtifact(secondaryValue);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    public static class HomuraAkemiPower extends PCLPower
    {
        private final AbstractCard sourceCard;

        public HomuraAkemiPower(AbstractPlayer owner, AbstractCard sourceCard, int amount)
        {
            super(owner, HomuraAkemi.DATA);

            this.amount = amount;
            this.sourceCard = sourceCard;
            this.isTurnBased = true;
            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLActions.Bottom.SelectFromPile(name, 1, player.masterDeck)
                    .SetFilter(c -> !c.cardID.equals(sourceCard.cardID))
                    .SetOptions(false, false)
                    .AddCallback(cards -> PCLActions.Bottom.MakeCardInHand(PCLGameUtilities.Imitate(cards.get(0))));
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.ReducePower(this, 1);
        }
    }


}
