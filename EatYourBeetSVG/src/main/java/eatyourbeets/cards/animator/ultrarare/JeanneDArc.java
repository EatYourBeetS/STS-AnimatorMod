package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class JeanneDArc extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(JeanneDArc.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate);

    public JeanneDArc()
    {
        super(DATA);

        Initialize(20, 0, 2);
        SetUpgrade(27, 0, 0);

        SetAffinity_Star(1);

        SetLoyal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Star(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.GOLD).SetSoundPitch(0.6f, 0.7f);

        GameActions.Bottom.ChangeStance(CalmStance.STANCE_ID);
        GameActions.Bottom.ChangeStance(DivinityStance.STANCE_ID);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile)
            .SetOptions(true, true)
           .SetFilter(c -> GameUtilities.HasAffinity(c, Affinity.Light) || GameUtilities.HasAffinity(c, Affinity.Dark))
            .AddCallback( cards -> {
                for (AbstractCard card : cards)
                {
                    card.setCostForTurn(0);
                }
            });
        }
    }
}