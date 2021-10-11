package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class JeanneDArc extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(JeanneDArc.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate);

    public JeanneDArc()
    {
        super(DATA);

        Initialize(11, 0, 8);
        SetUpgrade(2, 0, 0);

        SetAffinity_Fire(1);
        SetAffinity_Light(2, 0, 2);

        SetLoyal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.GOLD).SetSoundPitch(0.6f, 0.7f);
        GameActions.Bottom.RaiseLightLevel(1, true);
        GameActions.Bottom.PurgeFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(GameUtilities::IsHindrance);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainTemporaryHP(magicNumber);
            GameActions.Bottom.GainArtifact(1);
        }
    }
}