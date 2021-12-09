package eatyourbeets.cards.animator.series.MadokaMagica;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Kyubey extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kyubey.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
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

        SetPurge(true);
        GraveField.grave.set(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainEnergy(secondaryValue);

        GameActions.Bottom.SelectFromPile(name, 999, player.hand, player.drawPile, player.discardPile)
                .SetOptions(true, true)
                .SetFilter(GameUtilities::HasCooldown)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        if (GameUtilities.HasCooldown(c)) {
                            ((EYBCard) c).cooldown.ResetCooldown();
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
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.CreateGriefSeeds(secondaryValue);
        }
    }
}