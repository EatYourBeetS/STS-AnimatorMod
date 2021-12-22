package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.powers.replacement.AntiArtifactSlowPower;
import pinacolada.powers.replacement.PCLConstrictedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AnarchyStocking extends PCLCard implements OnPurgeSubscriber
{
    public static final PCLCardData DATA = Register(AnarchyStocking.class).SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.PantyStocking);

    public AnarchyStocking()
    {
        super(DATA);

        Initialize(0, 0, 4);
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
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            int poison = PCLGameUtilities.GetPowerAmount(PoisonPower.POWER_ID);
            if (poison > 0) {
                PCLActions.Bottom.RemovePower(player, player, PoisonPower.POWER_ID);
                PCLActions.Bottom.ApplyPoison(TargetHelper.Enemies(), poison);
            }

            int constricted = PCLGameUtilities.GetPowerAmount(PCLConstrictedPower.POWER_ID);
            if (constricted > 0) {
                PCLActions.Bottom.RemovePower(player, player, PCLConstrictedPower.POWER_ID);
                PCLActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), constricted);
            }

            int dd = PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID);
            if (dd > 0) {
                PCLActions.Bottom.RemovePower(player, player, DelayedDamagePower.POWER_ID);
                for (AbstractCreature cr : PCLGameUtilities.GetAllCharacters(true)) {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(player, cr, dd, AttackEffects.CLAW);
                }
            }
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new AntiArtifactSlowPower(m, 1));
        PCLActions.Bottom.ApplyPoison(TargetHelper.Player(), magicNumber);
        PCLActions.Bottom.ApplyConstricted(TargetHelper.Player(), magicNumber);
    }
}