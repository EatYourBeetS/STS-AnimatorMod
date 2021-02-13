package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Minori extends AnimatorCard {
    public static final EYBCardData DATA = Register(Minori.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Minori() {
        super(DATA);

        Initialize(0, 6, 50);
        SetUpgrade(0, 1, 25);

        SetSpellcaster();
        SetCooldown(4, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            ShuffleToTopOfDeck();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        ShuffleToTopOfDeck();
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Callback(c -> {
            int blockToGain = (int)(player.currentBlock * ((double)magicNumber/(double)100));

            GameActions.Bottom.GainBlock(blockToGain);
            GameActions.Bottom.VFX(new RainbowCardEffect());
        });
    }

    private void ShuffleToTopOfDeck()
    {
        flash();
        GameActions.Last.MoveCard(this, player.drawPile);
    }
}