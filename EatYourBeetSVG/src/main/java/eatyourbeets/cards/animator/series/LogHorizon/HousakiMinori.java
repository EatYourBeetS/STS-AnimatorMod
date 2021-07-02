package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;

public class HousakiMinori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiMinori.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public HousakiMinori()
    {
        super(DATA);

        Initialize(0, 6, 15);
        SetUpgrade(0, 1, 0);

        SetCooldown(4, -1, this::OnCooldownCompleted);
        SetSynergy(Synergies.LogHorizon);
        SetAlignment(0, 0, 1, 2, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
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
        GameActions.Bottom.StackPower(new EnchantedArmorPower(player, magicNumber));
    }

    private void ShuffleToTopOfDeck()
    {
        GameActions.Last.MoveCard(this, player.drawPile);
    }
}