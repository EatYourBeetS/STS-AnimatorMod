package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.MadokaKaname_KriemhildGretchen;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(MadokaKaname_KriemhildGretchen.DATA));
                data.AddPreview(new MadokaKaname_KriemhildGretchen(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
                data.AddPreview(AffinityToken.GetCard(Affinity.Light), true);
            });

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetAffinityRequirement(Affinity.Light, 6);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryArtifact(magicNumber);
        GameActions.Bottom.ObtainAffinityToken(Affinity.Light, upgraded)
        .AddCallback(c -> GameActions.Bottom.Motivate(c, 1));

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            GameActions.Bottom.ExhaustFromPile(name, 999, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(c -> c.type == CardType.CURSE)
            .AddCallback(cards -> GameActions.Bottom.GainIntangible(secondaryValue));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return super.CheckSpecialConditionLimited(tryUse, super::CheckSpecialCondition);
    }
}
