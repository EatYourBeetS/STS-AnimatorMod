package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.utilities.PCLActions;

public class Overheat extends PCLCard
{
    public static final PCLCardData DATA = Register(Overheat.class)
            .SetStatus(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(), false));

    public Overheat()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard) {
            PCLActions.Bottom.Draw(1);
            PCLActions.Bottom.MakeCardInDrawPile(new Status_Burn());
        }
    }

    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateSemiLimited(this.cardID)) {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, this.magicNumber, AttackEffects.FIRE);
        }

    }
}