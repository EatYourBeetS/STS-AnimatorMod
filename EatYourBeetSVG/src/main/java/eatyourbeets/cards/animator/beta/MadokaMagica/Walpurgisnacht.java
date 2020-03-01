package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Walpurgisnacht extends AnimatorCard_UltraRare implements Spellcaster {
    public static final EYBCardData DATA = Register(Walpurgisnacht.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
        .AddCallback(orbCores ->
        {
            if (orbCores != null && orbCores.size() > 0)
            {
                for (AbstractCard c : orbCores)
                {
                    c.applyPowers();
                    c.use(AbstractDungeon.player, null);
                }
            }
        }));

        MoveSpellcasters(p.discardPile, p.drawPile);

        if (upgraded)
        {
            MoveSpellcasters(p.exhaustPile, p.drawPile);
        }

        GameActions.Bottom.StackPower(new WalpurgisnachtPower(p, this.magicNumber));
    }

    private void MoveSpellcasters(CardGroup source, CardGroup destination)
    {
        float duration = 0.3f;

        for (AbstractCard card : source.group)
        {
            if (card instanceof Spellcaster)
            {
                GameActions.Top.MoveCard(card, source, destination)
                .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }
    }

    public static class WalpurgisnachtPower extends AnimatorPower
    {
        public static final String POWER_ID = CreateFullID(WalpurgisnachtPower.class.getSimpleName());

        public WalpurgisnachtPower(AbstractPlayer owner, int amount) {
            super(owner, POWER_ID);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            int numTimesEvoke = player.discardPile.getCardsOfType(CardType.CURSE).size() + GetSpellcasterCount(player.discardPile);

            if (numTimesEvoke > 0)
            {
                GameActions.Bottom.Add(new EvokeOrbAction(numTimesEvoke));
            }
        }

        private int GetSpellcasterCount(CardGroup group)
        {
            int count = 0;

            for (AbstractCard card : group.group)
            {
                if (card instanceof Spellcaster)
                {
                    count++;
                }
            }

            return count;
        }
    }
}