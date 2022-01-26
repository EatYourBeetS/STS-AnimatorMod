package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Apparition extends PCLCard
{
    public static final PCLCardData DATA = Register(Apparition.class)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Apparition()
    {
        super(DATA);

        Initialize(0, 0, 99, 1);
        SetUpgrade(0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetCostUpgrade(-1);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Bottom.StackPower(new IntangiblePlayerPower(mo, secondaryValue));
        }
        PCLActions.Bottom.StackPower(new IntangiblePlayerPower(p, secondaryValue));
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryStrength, -magicNumber).IgnoreArtifact(true);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryDexterity, -magicNumber).IgnoreArtifact(true);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryFocus, -magicNumber).IgnoreArtifact(true);

        PCLActions.Bottom.MoveCards(p.hand, p.discardPile)
                .SetFilter(c -> c instanceof Apparition)
                .ShowEffect(true, true, 0.25f)
                .AddCallback(cards -> {
                    PCLActions.Bottom.Draw(cards.size());
                });
    }
}