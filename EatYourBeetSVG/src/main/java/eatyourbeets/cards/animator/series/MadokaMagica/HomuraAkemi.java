package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.HomuraAkemi_Homulily;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

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

        Initialize(0, 0, 2, 3);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetExhaust(true);
        SetEthereal(true);

        SetAffinityRequirement(Affinity.Light,4);
        SetCooldown(0, 0, HomuraAkemi_Homulily::new);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.PurgeFromPile(name,1,player.hand).SetFilter(c -> c.type.equals(CardType.CURSE)).AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        GameActions.Bottom.ApplyPower(new HomuraAkemiPower(player, this, magicNumber));
                    }
                });

        if (CheckAffinity(Affinity.Light) && info.TryActivateLimited()) {
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
                    .AddCallback(cards -> GameActions.Bottom.MakeCardInHand(cards.get(0)).AddCallback(ca -> {
                        ca.purgeOnUse = true;
                        CostModifiers.For(ca).Set(-1);
                    }));
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.ReducePower(this, 1);
        }
    }


}
