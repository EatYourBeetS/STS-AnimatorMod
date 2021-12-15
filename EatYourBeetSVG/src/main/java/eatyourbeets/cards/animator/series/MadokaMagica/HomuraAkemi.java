package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.HomuraAkemi_Homulily;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
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

        SetDelayed(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 8);
        SetSoul(2, 0, HomuraAkemi_Homulily::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup[] choices = upgraded ? new CardGroup[] {player.hand,player.drawPile,player.discardPile,player.exhaustPile} : new CardGroup[] {player.hand,player.drawPile,player.discardPile};
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.PurgeFromPile(name,1,choices).SetFilter(c -> CardType.CURSE.equals(c.type)).AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        GameActions.Bottom.ApplyPower(new HomuraAkemiPower(player, this, magicNumber));
                    }
                });

        if (TrySpendAffinity(Affinity.Light) && info.TryActivateLimited()) {
            GameActions.Bottom.GainArtifact(secondaryValue);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    public static class HomuraAkemiPower extends AnimatorPower
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

            GameActions.Bottom.SelectFromPile(name, 1, player.masterDeck)
                    .SetFilter(c -> !c.cardID.equals(sourceCard.cardID))
                    .SetOptions(false, false)
                    .AddCallback(cards -> GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(cards.get(0))));
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.ReducePower(this, 1);
        }
    }


}
