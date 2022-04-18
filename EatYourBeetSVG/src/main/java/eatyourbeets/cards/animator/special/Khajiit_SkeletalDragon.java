package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Khajiit_SkeletalDragon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Khajiit_SkeletalDragon.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Overlord);

    private AbstractCard summoner;

    public Khajiit_SkeletalDragon()
    {
        this(null);
    }

    public Khajiit_SkeletalDragon(AbstractCard summoner)
    {
        super(DATA);

        Initialize(32, 8, 2);
        SetUpgrade(4, 0, 1);

        SetAffinity_Dark(2);
        SetAffinity_Red(2);

        this.summoner = summoner;
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (magicNumber * GameUtilities.GetOrbCount(Dark.ORB_ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DARK)
        .SetSoundPitch(0.4f, 0.5f)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Claw(c.hb, Color.VIOLET, Color.BLACK).SetScale(2f)).duration * 0.6f);
        GameActions.Bottom.ShakeScreen(0.4f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);

        GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> c != summoner)
        .AddCallback(() -> summoner = null);
    }
}