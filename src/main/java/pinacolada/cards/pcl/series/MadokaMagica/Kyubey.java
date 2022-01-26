package pinacolada.cards.pcl.series.MadokaMagica;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Kyubey extends PCLCard
{
    public static final PCLCardData DATA = Register(Kyubey.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public Kyubey()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
        SetAffinity_Silver(1);

        SetEthereal(true);
        SetPurge(true);
        GraveField.grave.set(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(magicNumber);
        PCLActions.Bottom.GainEnergy(secondaryValue);

        PCLActions.Bottom.SelectFromPile(name, 999, player.hand, player.drawPile, player.discardPile)
                .SetOptions(true, true)
                .SetFilter(PCLGameUtilities::HasCooldown)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        if (PCLGameUtilities.HasCooldown(c)) {
                            ((PCLCard) c).cooldown.ResetCooldown();
                            c.flash();
                        }
                    }
                });
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.CreateGriefSeeds(secondaryValue);
        }
    }
}