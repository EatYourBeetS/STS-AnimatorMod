package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.powers.replacement.AnimatorConstrictedPower;
import eatyourbeets.powers.replacement.AntiArtifactSlowPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class AnarchyStocking extends AnimatorCard implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(AnarchyStocking.class).SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.PantyStocking);

    public AnarchyStocking()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetHaste(true);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();
        if (isEthereal) {
            int amount = GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID);
            if (amount > 0) {
                GameActions.Bottom.RemovePower(player, player, DelayedDamagePower.POWER_ID);
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int amount = GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID);
        if (amount > 0) {
            GameActions.Bottom.RemovePower(player, player, DelayedDamagePower.POWER_ID);
        }
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            int poison = GameUtilities.GetPowerAmount(PoisonPower.POWER_ID);
            if (poison > 0) {
                GameActions.Bottom.RemovePower(player, player, PoisonPower.POWER_ID);
                GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), poison);
            }

            int constricted = GameUtilities.GetPowerAmount(AnimatorConstrictedPower.POWER_ID);
            if (constricted > 0) {
                GameActions.Bottom.RemovePower(player, player, AnimatorConstrictedPower.POWER_ID);
                GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), constricted);
            }
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AntiArtifactSlowPower(m, 1));
        GameActions.Bottom.ApplyPoison(TargetHelper.Player(), magicNumber);
        GameActions.Bottom.ApplyConstricted(TargetHelper.Player(), magicNumber);
    }
}