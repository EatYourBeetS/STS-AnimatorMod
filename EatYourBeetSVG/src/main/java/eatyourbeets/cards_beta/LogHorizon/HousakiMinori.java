package eatyourbeets.cards_beta.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.EnchantedArmorPlayerPower;
import eatyourbeets.utilities.GameActions;

public class HousakiMinori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiMinori.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public HousakiMinori()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 1, 0);

        SetCooldown(4, -1, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            ShuffleToTopOfDeck();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            ShuffleToTopOfDeck();
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new EnchantedArmorPlayerPower(player, magicNumber));
    }

    private void ShuffleToTopOfDeck()
    {
        GameActions.Last.MoveCard(this, player.drawPile);
    }
}